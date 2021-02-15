package alo.spring.batch.rooster;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

class Fruit {
    private String name;

    public Fruit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

class Apple extends Fruit {

    private final String arbre = "Pommier";

    public Apple() {
        super("pomme");
    }

    public Apple(String name) {
        super(name);
    }

    public void presse() {
        System.out.println("Jus de pomme!! (" + arbre + "s de france)");
    }
}

@Slf4j
public class CheckJava {
    @Test
    public void dateTests() {
        System.out.println("Hello World!!");

        Fruit fruit = new Fruit("fruit");

        Apple apple = new Apple();

        List<Fruit> fruitBasket = new ArrayList<>();
        fruitBasket.add(apple);
        fruitBasket.add(fruit);

        System.out.println("Pannier de fruits:");
        System.out.println(fruitBasket);

        List<? super Apple> appleBasket = new ArrayList<>();
        appleBasket.add(apple);
//        appleBasket.add(fruit);

        for (Object f :
                appleBasket) {
            System.out.println(f.toString());
        }

        System.out.println("Pannier de pomme:");
        System.out.println(appleBasket);
    }
}
