package comp.finalproject.admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sales")
public class Sale implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_item", referencedColumnName = "id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonIgnoreProperties("sales")
    private User user;
    private int quantity;
    private float subtotal;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String status;
    @Column(name = "metode_pembayaran")
    private String metodePembayaran;
    @Column(name = "proof_of_payment")
    private String proofOfPayment;
    private boolean deleted = false;
}
