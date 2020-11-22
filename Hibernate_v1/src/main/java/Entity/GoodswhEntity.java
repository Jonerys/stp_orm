package Entity;

import javax.persistence.*;

@Entity
@Table(name = "goodswh", schema = "goods")
public class GoodswhEntity {
    private int id;
    private WarehousesEntity warehousesByIdWh;
    private GoodsMainEntity goodsMainByIdGd;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoodswhEntity that = (GoodswhEntity) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "id_wh", referencedColumnName = "id", nullable = false)
    public WarehousesEntity getWarehousesByIdWh() {
        return warehousesByIdWh;
    }

    @ManyToOne
    @JoinColumn(name = "id_gd", referencedColumnName = "id", nullable = false)
    public GoodsMainEntity getGoodsMainByIdGd() { return goodsMainByIdGd; }

    public void setWarehousesByIdWh(WarehousesEntity warehousesByIdWh) {
        this.warehousesByIdWh = warehousesByIdWh;
    }
    public void setGoodsMainByIdGd(GoodsMainEntity goodsmainByIdGd) {
        this.goodsMainByIdGd = goodsmainByIdGd;
    }

    public String toString() {
        return id + " " + this.goodsMainByIdGd.toString_noid() + " | " + this.warehousesByIdWh.toString_noid();
    }
}
