package lk.choizy.company.CompanyBraunch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import lk.choizy.company.Company.CompanyHelper;
import lk.choizy.company.Orders;
import lk.choizy.company.R;

public class OrderFinishView_BS extends BottomSheetDialogFragment {

    Orders orders;
    TextView studentId,studentName,studentNumber,orderView_PaymentMethod,orderView_Address,totalPrice;
    ImageView profileImg;
    Button orderFisnhBtn;
    RecyclerView orderView_OrderList;
    OrderListItemAdapter adapter;



    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_view,container,false);
        studentId = view.findViewById(R.id.orderView_StudentID);
        studentName = view.findViewById(R.id.orderView_StudentName);
        studentNumber = view.findViewById(R.id.orderView_StudentMobile);
        orderView_PaymentMethod = view.findViewById(R.id.orderView_PaymentMethod);
        orderView_Address = view.findViewById(R.id.orderView_Address);
        orderFisnhBtn = view.findViewById(R.id.orderView_OrderFisnhBtn);
        orderView_OrderList = view.findViewById(R.id.orderView_OrderList);
        profileImg = view.findViewById(R.id.orderView_StudentImg);
        totalPrice = view.findViewById(R.id.orderView_totalPrice);
        adapter = new OrderListItemAdapter();

        orderView_OrderList.setLayoutManager(new LinearLayoutManager(getContext()));
        orderView_OrderList.setAdapter(adapter);
        adapter.setList(orders.getCartList());
        adapter.notifyDataSetChanged();

        Picasso.get().load(orders.getStudent().getImageURL()).into(profileImg);
        studentId.setText(orders.getStudentKey());
        totalPrice.setText(CompanyHelper.getFormattedPrice(orders.getTotal()));
        studentName.setText(orders.getStudentName());
        studentNumber.setText(orders.getStudentMobile());
        orderView_PaymentMethod.setText("Payment Method - "+orders.getPaymentType());
      //  orderView_Address.setText("Address -"+orders.getDelivery());
        if(orders.getOrderType().equals("Delivery")){
            orderView_Address.setText("Address -"+orders.getDelivery());
        }else{
            orderView_Address.setText("Order Type - "+orders.getOrderType());
        }

        orderFisnhBtn.setVisibility(View.INVISIBLE);
        orderFisnhBtn.setEnabled(false);



        return view;
    }

    public OrderFinishView_BS(Orders orders) {
        this.orders = orders;
    }
}
