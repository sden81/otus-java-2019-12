package core.model;

import javax.persistence.*;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @OneToOne(targetEntity = Address.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<Phone> phones;

    public User() {
    }

    public User(long id, String name, String password, Address address, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.address = address;
        this.phones = phones;
    }

    public User(String name, String password, Address address, Set<Phone> phones) {
        this.id = 0;
        this.name = name;
        this.password = password;
        this.address = address;
        this.phones = phones;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Address getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public Optional<Phone> getFirstPhone() {
        Iterator<Phone> iterator = phones.iterator();

        return Optional.of(iterator.next());
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }
}
