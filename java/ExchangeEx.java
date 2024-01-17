package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

public class ExchangeEx {

    public static void main(String[] args) {

        Exchanger<Action> exchanger = new Exchanger<>();
        List<Action> friendsAction = new ArrayList<>();
        friendsAction.add(Action.NOZHNICY);
        friendsAction.add(Action.BUMAGA);
        friendsAction.add(Action.NOZHNICY);

        List<Action> myAction = new ArrayList<>();
        myAction.add(Action.BUMAGA);
        myAction.add(Action.KAMEN);
        myAction.add(Action.KAMEN);

        new Friend("pet", friendsAction, exchanger);
        new Friend("my", myAction, exchanger);
    }
}

enum Action {
    KAMEN, NOZHNICY, BUMAGA;
}

class Friend extends Thread{
    private String name;
    private List<Action> myActions;
    private Exchanger<Action> exchanger;

    public Friend(String name, List<Action> actions, Exchanger<Action> exchanger){
        this.name = name;
        this.exchanger = exchanger;
        this.myActions = actions;
        this.start();
    }
    public void getWinner(Action myAction, Action friendAction){
        if((myAction == Action.KAMEN && friendAction == Action.NOZHNICY)
        || (myAction == Action.NOZHNICY && friendAction == Action.BUMAGA)
        || (myAction == Action.BUMAGA && friendAction == Action.KAMEN)){
            System.out.println("WINS " + name);
        }
    }
    public void run(){

        Action reply;
        for(Action action: myActions){
            try {
                reply = exchanger.exchange(action);
                getWinner(action, reply);
                sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}