package pl.kurs.zad1.service;


import pl.kurs.zad1.model.Person;

import java.io.*;

import java.util.ArrayList;
import java.util.List;

import java.util.function.Function;
import java.util.function.Predicate;

public class ObjectContainer<T> {

    private Node<T> head;
    private Predicate<T> condition;

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public ObjectContainer(Predicate<T> condition) {
        this.condition = condition;
    }

    public boolean add(T object) {
        if (!condition.test(object)) {
            return false;
        }
        Node<T> newNode = new Node<>(object);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
        return true;
    }

    public List<T> getWithFilter(Predicate<T> filter) {
        List<T> resultList = new ArrayList<>();
        Node<T> temp = head;
        while (temp != null) {
            if (filter.test(temp.data)) {
                resultList.add(temp.data);
            }
            temp = temp.next;
        }
        return resultList;
    }

    public void removeIf(Predicate<T> filter) {
        while (head != null && filter.test(head.data)) {
            head = head.next;
        }


        if (head == null) {
            return;
        }

        Node<T> current = head;
        while (current.next != null) {
            if (filter.test(current.next.data)) {
                current.next = current.next.next;
            } else {
                current = current.next;
            }
        }
    }

    public void storeToFile(String fileName) throws IOException {
        storeToFile(fileName, obj -> true, obj -> obj.toString());
    }


    public void storeToFile(String fileName, Predicate<T> filter, Function<T, String> formatter) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Node<T> temp = head;
            while (temp != null) {
                if (filter.test(temp.data)) {
                    writer.write(formatter.apply(temp.data) + "\n");
                }
                temp = temp.next;
            }
        }
    }


    public static ObjectContainer<Person> fromFile(String fileName) throws IOException, ClassNotFoundException {
        ObjectContainer<Person> container = new ObjectContainer<>(obj -> true);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String name = line.split("name='")[1].split("'")[0];
                String city = line.split("city='")[1].split("'")[0];
                int age = Integer.parseInt(line.split("age=")[1].split("}")[0].trim());

                Person person = new Person(name, city, age);
                container.add(person);
            }
        }

        return container;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data.toString()).append("\n");
            current = current.next;
        }
        return sb.toString();
    }
}