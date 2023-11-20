package comp.finalproject.admin.service;

import comp.finalproject.admin.entity.Item;
import org.javers.core.ChangesByCommit;
import org.javers.core.Javers;
import org.javers.core.commit.CommitMetadata;
import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AuditService {
    @Autowired
    private Javers javers;

    public ResponseEntity<List<Map<String, Object>>> findAllChange() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(Item.class);
        List<ChangesByCommit> changesByCommits = javers.findChanges(jqlQuery.build()).groupByCommit();
        List<Map<String, Object>> changeDetailsList = new ArrayList<>();

        for (ChangesByCommit commitChanges : changesByCommits) {
            CommitMetadata commit = commitChanges.getCommit();

            // Extract commitDate, author
            LocalDateTime commitDate = commit.getCommitDate();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = commitDate.format(myFormatObj);

            String author = commit.getAuthor();

            List<Change> allChanges = commitChanges.get();
            Map<String, Object> changeDetails = new HashMap<>();
            changeDetails.put("commitDate", formattedDate);
            changeDetails.put("author", author);

            List<String> changedProperties = new ArrayList<>();
            Map<String, Object> left = new HashMap<>();
            Map<String, Object> right = new HashMap<>();
            String changeType = null; // Deklarasikan di luar loop

            for (Change change : allChanges) {
                if (change instanceof ValueChange) {
                    ValueChange valueChange = (ValueChange) change;
                    String property = valueChange.getPropertyName();
                    Object originalValue = valueChange.getLeft();
                    Object newValue = valueChange.getRight();

                    // Skip perubahan pada properti ID
                    if ("id".equals(property)) {
                        continue;
                    }

                    if (originalValue == null && newValue != null) {
                        // Jika nilai awal null dan nilai baru bukan null, ini adalah inisialisasi
                        changeType = "Initial";
                    } else if (originalValue != null && newValue != null) {
                        // Jika kedua nilai ada dan berbeda, ini adalah update
                        if (!originalValue.equals(newValue)) {
                            changeType = "Update";
                        }
                    }
                    if ("deleted".equals(property) && Boolean.TRUE.equals(newValue)) {
                        // Ini adalah penghapusan
                        changeType = "Delete";
                    } else if ("deleted".equals(property) && Boolean.FALSE.equals(newValue)) {
                        changeType = "Restore";
                    }

                    // Populate changedProperties and state
                    changedProperties.add(property);
                    left.put(property, originalValue);
                    right.put(property, newValue);
                }
            }


            // Determine the changeType based on changed properties
            changeDetails.put("changedProperties", changedProperties);
            changeDetails.put("left", left);
            changeDetails.put("right", right);
            changeDetails.put("changeType", changeType);
            changeDetailsList.add(changeDetails);
        }

        if (!changeDetailsList.isEmpty()) {
            return new ResponseEntity<>(changeDetailsList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
    }
}
