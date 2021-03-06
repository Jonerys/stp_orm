package Entity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "warehouses", schema = "goods")
public class WarehousesEntity {

    private int id;
    private String name;

    /*@ManyToMany
    @JoinTable (name="goodswh",
            joinColumns=@JoinColumn (name="id_wh"),
            inverseJoinColumns=@JoinColumn(name="id_gd"))
    private Set<GoodsMainEntity> goodsMainEntitySet;*/

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WarehousesEntity that = (WarehousesEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return id + "\t" + name;
    }
    public String toString_noid() {
        return name;
    }
}
