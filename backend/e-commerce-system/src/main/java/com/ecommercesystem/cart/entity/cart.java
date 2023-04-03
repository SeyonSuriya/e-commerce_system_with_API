package com.ecommercesystem.cart.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "cart")
public class cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",length = 5)
    private int id;

    @Column(name = "userid",length = 5)
    private int userid;

    @Column(name = "item_id",length = 5)
    private int item_id;

    @Column(name = "quantity",length = 5)
    private int quantity;


    public cart(int id, int userid, int item_id, int quantity) {
        this.id = id;
        this.userid = userid;
        this.item_id = item_id;
        this.quantity = quantity;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }





    public cart() {
    }
}
