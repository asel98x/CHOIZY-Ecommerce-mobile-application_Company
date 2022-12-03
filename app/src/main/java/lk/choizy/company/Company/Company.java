package lk.choizy.company.Company;

import com.google.firebase.database.Exclude;

public class Company {
    String company_category,company_email,company_mobile,company_name,company_password,imageURL,key,companyAbout,companyFeatures,companyTerms,companyDiscountRange;

    public Company(String company_category, String company_email, String company_mobile, String company_name, String company_password, String imageURL) {
        this.company_category = company_category;
        this.company_email = company_email;
        this.company_mobile = company_mobile;
        this.company_name = company_name;
        this.company_password = company_password;
        this.imageURL = imageURL;
    }

    public Company() {

    }

    public String getCompany_category() {
        return company_category;
    }

    public void setCompany_category(String company_category) {
        this.company_category = company_category;
    }

    public String getCompany_email() {
        return company_email;
    }

    public void setCompany_email(String company_email) {
        this.company_email = company_email;
    }

    public String getCompany_mobile() {
        return company_mobile;
    }

    public void setCompany_mobile(String company_mobile) {
        this.company_mobile = company_mobile;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_password() {
        return company_password;
    }

    public void setCompany_password(String company_password) {
        this.company_password = company_password;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCompanyAbout() {
        return companyAbout;
    }

    public void setCompanyAbout(String companyAbout) {
        this.companyAbout = companyAbout;
    }

    public String getCompanyFeatures() {
        return companyFeatures;
    }

    public void setCompanyFeatures(String companyFeatures) {
        this.companyFeatures = companyFeatures;
    }

    public String getCompanyDiscountRange() {
        return companyDiscountRange;
    }

    public void setCompanyDiscountRange(String companyDiscountRange) {
        this.companyDiscountRange = companyDiscountRange;
    }

    public String getCompanyTerms() {
        return companyTerms;
    }

    public void setCompanyTerms(String companyTerms) {
        this.companyTerms = companyTerms;
    }
}
