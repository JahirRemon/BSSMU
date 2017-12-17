package com.example.mdjahirulislam.bssmu_demo.model;

/**
 * Created by mdjahirulislam on 14/12/17.
 */

public class UserModel {

    private String user_unique_id;
    private String user_full_name;
    private String user_name;
    private String user_avatar;
    private String user_email;
    private String password;
    private String role;
    private String doctor_id;


    public UserModel(String user_unique_id, String user_full_name, String user_name, String user_avatar, String user_email, String password, String role, String doctor_id) {
        this.user_unique_id = user_unique_id;
        this.user_full_name = user_full_name;
        this.user_name = user_name;
        this.user_avatar = user_avatar;
        this.user_email = user_email;
        this.password = password;
        this.role = role;
        this.doctor_id = doctor_id;
    }

    public UserModel(String user_email, String password) {
        this.user_email = user_email;
        this.password = password;
    }

    public UserModel() {
    }

    public String getUser_unique_id() {
        return user_unique_id;
    }

    public String getUser_full_name() {
        return user_full_name;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "user_unique_id='" + user_unique_id + '\'' +
                ", user_full_name='" + user_full_name + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_avatar='" + user_avatar + '\'' +
                ", user_email='" + user_email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", doctor_id='" + doctor_id + '\'' +
                '}';
    }
}
