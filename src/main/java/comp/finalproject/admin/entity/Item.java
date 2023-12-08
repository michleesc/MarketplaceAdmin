package comp.finalproject.admin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.javers.core.metamodel.annotation.TypeName;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TypeName("Table Item")
@Table(name = "item")
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String type;
    private int quantity;
    private int amount;
    @Column(name = "total_sold")
    private int totalSold;
    @Column(name = "image_path")
    private String imagePath;
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm z", timezone = "Asia/Jakarta")
    private Date createdAt;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm z", timezone = "Asia/Jakarta")
    private Date deletedAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm z", timezone = "Asia/Jakarta")
    @UpdateTimestamp
    private Date updatedAt;

    private boolean deleted = false;
}