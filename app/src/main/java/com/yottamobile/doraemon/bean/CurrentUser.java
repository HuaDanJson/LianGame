package com.yottamobile.doraemon.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import cn.bmob.v3.BmobUser;

@Entity
public class CurrentUser extends BmobUser {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "CurrentUser")

    private int score;
    private int helperCards;
    private int doubleCards;
    private int textColor;

    @Generated(hash = 2078650344)
    public CurrentUser(Long id, int score, int helperCards, int doubleCards,
            int textColor) {
        this.id = id;
        this.score = score;
        this.helperCards = helperCards;
        this.doubleCards = doubleCards;
        this.textColor = textColor;
    }

    @Generated(hash = 1481753967)
    public CurrentUser() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return this.score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public int getHelperCards() {
        return this.helperCards;
    }

    public void setHelperCards(int helperCards) {
        this.helperCards = helperCards;
    }

    public int getDoubleCards() {
        return this.doubleCards;
    }

    public void setDoubleCards(int doubleCards) {
        this.doubleCards = doubleCards;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "id=" + id +
                ", score=" + score +
                ", helperCards=" + helperCards +
                ", doubleCards=" + doubleCards +
                ", textColor=" + textColor +
                '}';
    }

    public void setScore(int score) {
        this.score = score;
    }
}
