package com.guide.tezproject.fragment.util;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guide.tezproject.R;
import com.guide.tezproject.database.DatabaseHelper;
import com.guide.tezproject.entity.GezilecekYer;
import com.guide.tezproject.entity.Nokta;
import com.guide.tezproject.fragment.ContentFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    ArrayList<Product> mProductList;
    LayoutInflater inflater;
    private final Context context;

    DatabaseHelper databaseHelper;


    public ProductAdapter(Context context, ArrayList<Product> ProductList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mProductList = ProductList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_product_card, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product selectedProduct = mProductList.get(position);
        holder.setData(selectedProduct, position);
        Picasso.get()
                .load(selectedProduct.getImageID())
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productDescription;
        ImageView productImage, deleteproduct, addproduct;
        String name, city;
        Double latitude, longitude;
        GezilecekYer gezilecekYer;


        public MyViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productDescription = (TextView) itemView.findViewById(R.id.productDescription);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            addproduct = (ImageView) itemView.findViewById(R.id.addproduct);
            deleteproduct = (ImageView) itemView.findViewById(R.id.deleteproduct);

            addproduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    databaseHelper = new DatabaseHelper(context);
                    if(databaseHelper.insertPoint(name,city,latitude,longitude)) {
                        //Toast.makeText(context, name + " Rotaya eklendi", Toast.LENGTH_SHORT).show();
                        addproduct.setVisibility(View.INVISIBLE);
                        deleteproduct.setVisibility(View.VISIBLE);
                    }
                    else
                        Toast.makeText(context,name+ " Rotaya Eklenemedi-HATA!",Toast.LENGTH_SHORT).show();
                }
            });
            deleteproduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    databaseHelper = new DatabaseHelper(context);
                    if(!databaseHelper.deletePoint(name,city)){
                        //Toast.makeText(context,name + " rotadan cikarildi.",Toast.LENGTH_LONG).show();
                        deleteproduct.setVisibility(View.INVISIBLE);
                        addproduct.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(context,name+ " Rotadan Cikarilamadi-HATA !",Toast.LENGTH_SHORT).show();
                    }

                }
            });
            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();

                    FragmentTransaction fragmentTransaction =  appCompatActivity.getSupportFragmentManager().beginTransaction();
                    ContentFragment contentFragment = new ContentFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Gonder",gezilecekYer);

                    contentFragment.setArguments(bundle);

                    fragmentTransaction.replace(R.id.container,contentFragment).addToBackStack("tagg").commit();


                }
            });

        }

        public void setData(Product selectedProduct, int position) {

            name = selectedProduct.getProductName();
            city = selectedProduct.getProductCity();
            latitude = selectedProduct.getProductLatitude();
            longitude = selectedProduct.getProductLongitude();

            gezilecekYer = new GezilecekYer();
            gezilecekYer.setName(selectedProduct.getProductName());
            gezilecekYer.setKeywords(selectedProduct.getProductKeywords());
            gezilecekYer.setContent(selectedProduct.getProductContent());
            gezilecekYer.setTur(selectedProduct.getProductTur());
            gezilecekYer.setTurizmTur(selectedProduct.getProductTurizmTur());
            gezilecekYer.setNasilGidilir(selectedProduct.getProductNasilGidilir());
            gezilecekYer.setAdres(selectedProduct.getProductAdres());
            gezilecekYer.setZiyaretSaatleri(selectedProduct.getProductZiyaretSaatleri());
            gezilecekYer.setLatitude(selectedProduct.getProductLatitude());
            gezilecekYer.setLongitude(selectedProduct.getProductLongitude());
            gezilecekYer.setCountry(selectedProduct.getProductCountry());
            gezilecekYer.setCity(selectedProduct.getProductCity());
            gezilecekYer.setIlce(selectedProduct.getProductIlce());
            gezilecekYer.setIconUrl(selectedProduct.getImageID().toString());

            this.productName.setText(name);
            this.productDescription.setText(selectedProduct.getProductTurizmTur());
            this.productImage.setImageURI(selectedProduct.getImageID());

            databaseHelper = new DatabaseHelper(context);
            Nokta nokta = databaseHelper.getPoint(name,city);
            if(nokta != null){
                this.deleteproduct.setVisibility(View.VISIBLE);
                this.addproduct.setVisibility(View.INVISIBLE);
            }else{
                this.deleteproduct.setVisibility(View.INVISIBLE);
                this.addproduct.setVisibility(View.VISIBLE);
            }
        }

    }
}