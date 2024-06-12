package ua.edu.nung.pz.dao.entity;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private User user;
    private Map<Good, Integer> goods = new HashMap<>();

    public Cart() {
    }

    public Cart(User user, Map<Good, Integer> goods) {
        this.user = user;
        this.goods = goods;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<Good, Integer> getGoods() {
        return goods;
    }

    public void setGoods(Map<Good, Integer> goods) {
        this.goods = goods;
    }

    public void addGood(Good good, int quantity) {
        this.goods.put(good, quantity);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "user=" + user +
                ", goods=" + goods +
                '}';
    }
}