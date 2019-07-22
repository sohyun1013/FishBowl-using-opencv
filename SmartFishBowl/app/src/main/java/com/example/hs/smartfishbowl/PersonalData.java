package com.example.hs.smartfishbowl;

public class PersonalData {
   // private String member_id;
    private String member_temp;
    private String member_turb;
    private String member_x;
    private String member_y;
    private int member_lid;
    private String member_loc;
    private int member_vid;
    private String member_velo;

    //public String getMember_id() {
   //     return member_id;
    //}

    public String getMember_temp() {
        return member_temp;
    }

    public String getMember_turb() {
        return member_turb;
    }

    public String getMember_x(){return member_x;}

    public String getMember_y(){return member_y;}

    public int getMember_lid(){return member_lid;}

    public String getMember_loc(){return member_loc;}

    public int getMember_vid(){return member_vid;}

    public String getMember_velo(){return member_velo;}

   // public void setMember_id(String member_id) {
    //    this.member_id = member_id;
    //}

    public void setMember_temp(String member_temp) {
        this.member_temp = member_temp;
    }

    public void setMember_turb(String member_turb) {this.member_turb = member_turb; }

    public void setMember_x(String member_x){this.member_x=member_x;}

    public void setMember_y(String member_y){this.member_y=member_y;}

    public void setMember_lid(int member_lid){this.member_lid=member_lid;}

    public void setMember_loc(String member_loc){this.member_loc=member_loc;}

    public void setMember_vid(int member_vid){this.member_vid=member_vid;}

    public void setMember_velo(String member_velo){this.member_velo=member_velo;}
}
