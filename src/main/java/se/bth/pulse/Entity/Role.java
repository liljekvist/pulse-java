package se.bth.pulse.Entity;

import jakarta.persistence.*;

@Entity(name = "Role")
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String name;

    private String premissions; //site wide and structured like this "rwx" where r is read, w is write and x is full access

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPremissions() {
        return premissions;
    }

    public void setPremissions(String premissions) {
        this.premissions = premissions;
    }
}
