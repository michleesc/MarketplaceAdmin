package comp.finalproject.admin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TypeName("Table Item")
@Table(name = "item")
public class Item {
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
    @Column(name = "created_at")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm z", timezone = "Asia/Jakarta")
    @CreatedDate
    private Date createdAt;
    @Column(name = "deleted_at")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm z", timezone = "Asia/Jakarta")
    private Date deletedAt;
    @Column(name = "updated_at")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm z", timezone = "Asia/Jakarta")
    @LastModifiedDate
    private Date updatedAt;
    private boolean deleted = false;
}