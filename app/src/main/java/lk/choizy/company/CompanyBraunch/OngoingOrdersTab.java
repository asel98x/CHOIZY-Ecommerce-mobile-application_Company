package lk.choizy.company.CompanyBraunch;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import lk.choizy.company.BranchHelper;
import lk.choizy.company.Orders;
import lk.choizy.company.R;
import lk.choizy.company.Student;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OngoingOrdersTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OngoingOrdersTab extends Fragment implements OngoingOrderCardAdapter.onItemSelected, OrderView_BS.onOrderListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView orderListView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<Orders> list ;
    BranchHelper dbHelper;
    ValueEventListener ongoingOrderVEL;
    OngoingOrderCardAdapter adapter;
    int count = 0;

    public OngoingOrdersTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OngoingOrdersTab.
     */
    // TODO: Rename and change types and number of parameters
    public static OngoingOrdersTab newInstance(String param1, String param2) {
        OngoingOrdersTab fragment = new OngoingOrdersTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.ongoing_orders_tab, container, false);

        orderListView = view.findViewById(R.id.OngingOrderLV);
        list = new ArrayList<>();
        dbHelper = new BranchHelper(getContext());

        adapter = new OngoingOrderCardAdapter();

        adapter.setListener(this);

        orderListView.addItemDecoration(new OngoingOrderCardAdapter.OrderCardDecoration());

        orderListView.setAdapter(adapter);
        orderListView.setLayoutManager(new LinearLayoutManager(getContext()));

        ongoingOrderVEL = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                adapter.setList(list);
                adapter.notifyDataSetChanged();

                if(count <= dataSnapshot.getChildrenCount()){
                    newOrderNotification(dataSnapshot.getChildrenCount()-count);
                }

                count = (int) dataSnapshot.getChildrenCount();

                Iterator<DataSnapshot> iterator =  dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()){
                    DataSnapshot one = iterator.next();
                    Orders orders = one.getValue(Orders.class);
                    orders.setOrderID(one.getKey());

                    dbHelper.studentGetDetailsByKey(orders.getStudentKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            dbHelper.studentGetDetailsByKey(orders.getStudentKey()).removeEventListener(this);
                            Student student = dataSnapshot2.getValue(Student.class);
                            orders.setStudent(student);
                            list.add(orders);


                            if(!iterator.hasNext()){
                                Collections.reverse(list);
                                adapter.setList(list);
                                adapter.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };




        return view;
    }

    private void newOrderNotification(long l) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String body = "there are "+l+" orders";
            String title = "New orders";
            String C_ID = "Choizy Acdamic notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(C_ID, title, importance);
            channel.setDescription(body);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            if(getActivity() ==null){
                return;
            }
            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
//            NotificationChannel channel = new NotificationChannel(C_ID,"Choizy", NotificationManager.IMPORTANCE_HIGH);
            getActivity().getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification.Builder notification = new Notification.Builder(requireContext(),C_ID).setContentTitle(title).setContentText(body).setSmallIcon(R.mipmap.ic_launcher).setColor(getResources().getColor(R.color.blue));
            notificationManager.notify(3,notification.build());
        }
    }

    private void  loadOrder(){
        ongoingOrderVEL = dbHelper.getOngoingOrder().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void ItemSelected(int position) {
        OrderView_BS order = new OrderView_BS(list.get(position));
        order.setListener(this);
        order.show(getChildFragmentManager(),"Order Bottom Sheet");

    }

    @Override
    public void onResume() {
        //loadOrder();
        dbHelper.getOngoingOrder().addValueEventListener(ongoingOrderVEL);
        System.out.println("OnResume order");
        super.onResume();
    }

    @Override
    public void onStart() {
      //  dbHelper.getOngoingOrder().addValueEventListener(ongoingOrderVEL);
        System.out.println("onStart order");
        super.onStart();

    }

    @Override
    public void onStop() {
        dbHelper.getOngoingOrder().removeEventListener(ongoingOrderVEL);
        System.out.println("onStop order");
        super.onStop();
    }



    @Override
    public void onOrderUpdate(Orders order) {

        sendNotification(order);
        dbHelper.updateOrder(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Snackbar.make(getView(),"Order Finish",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotification(Orders order){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json=new JSONObject();
                    JSONObject dataJson=new JSONObject();
                    dataJson.put("body","Order ID"+order.getOrderID());
                    if(order.getOrderType().equals("Delivery")){
                        dataJson.put("title","You will receive your delivery soon");

                    }else{
                        dataJson.put("title","Order is ready to pick-up");
                    }


                    json.put("notification",dataJson);
                    json.put("to",order.getNotificationKey());
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization","key="+"AAAAMLOpVkM:APA91bFt4FAnoM4tx9XvSGuzJ4CBAVRgx34ZelPyqvcPuce8ELWGYrV0aDbI7PNEfA3N041g4GlQNO8loqzJCING_SKYeNt9kTzSqgjlBAKAoYhSCoYk5TSMg_EoxC4_89HpvwopGL6J")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                }catch (Exception e){
                    //Log.d(TAG,e+"");
                }
            }
        });
    }

    @Override
    public void onOrderFinish(Orders order) {
        dbHelper.FinishOrder(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Snackbar.make(getView(),"Order Complete",Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}