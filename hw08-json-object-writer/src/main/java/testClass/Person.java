package testClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Person {
    private String name;
    private int age;
    private boolean isCanFly = false;
    private double weight = 67.9;
    protected Person anotherPerson;
    private int[] favoriteNumbers;
    private Person[] friends;
    private List<Person> family;
    private Set<Person> enemies;
    private List<Integer> emptyList = new ArrayList<>();
    private int[] emptyArray = {};

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static Person createPerson() {
        var person = new Person("Test person", 21);

        person.setAnotherPerson(new Person("Another testClass.Person", 22));

        person.setFavoriteNumbers(new int[]{1, 2, 3});
        person.setFriends(new Person[]{
                new Person("friend1", 20),
                new Person("friend2", 23)
        });

        person.setFamily(new ArrayList<Person>());
        person.getFamily().add(new Person("brother", 12));
        person.getFamily().add(new Person("sister", 14));

        person.setEnemies(new HashSet<Person>());
        person.getEnemies().add(new Person("enemy1", 25));
        person.getEnemies().add(new Person("enemy2", 24));

        return person;
    }

    public Person getAnotherPerson() {
        return anotherPerson;
    }

    public void setAnotherPerson(Person anotherPerson) {
        this.anotherPerson = anotherPerson;
    }

    public int[] getFavoriteNumbers() {
        return favoriteNumbers;
    }

    public void setFavoriteNumbers(int[] favoriteNumbers) {
        this.favoriteNumbers = favoriteNumbers;
    }

    public Person[] getFriends() {
        return friends;
    }

    public void setFriends(Person[] friends) {
        this.friends = friends;
    }

    public List<Person> getFamily() {
        return family;
    }

    public void setFamily(List<Person> family) {
        this.family = family;
    }

    public Set<Person> getEnemies() {
        return enemies;
    }

    public void setEnemies(Set<Person> enemies) {
        this.enemies = enemies;
    }
}
