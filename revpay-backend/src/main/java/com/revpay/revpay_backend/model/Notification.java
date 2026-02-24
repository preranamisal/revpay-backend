//package com.revpay.revpay_backend.model;
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "notifications")
//public class Notification {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private Long userId;
//
//    private String title;
//
//    private String message;
//
//    private boolean isRead = false;
//
//    private LocalDateTime createdAt;
//
//    public Notification() {}
//
//    public Notification(Long userId, String title, String message) {
//        this.userId = userId;
//        this.title = title;
//        this.message = message;
//        this.createdAt = LocalDateTime.now();
//        this.isRead = false;
//    }
//
//    // getters & setters
//    public Long getId() { return id; }
//    public Long getUserId() { return userId; }
//    public void setUserId(Long userId) { this.userId = userId; }
//
//    public String getTitle() { return title; }
//    public void setTitle(String title) { this.title = title; }
//
//    public String getMessage() { return message; }
//    public void setMessage(String message) { this.message = message; }
//
//    public boolean isRead() { return isRead; }
//    public void setRead(boolean read) { isRead = read; }
//
//    public LocalDateTime getCreatedAt() { return createdAt; }
//}

package com.revpay.revpay_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;   // NEW

    private String title;

    private String message;

    @Column(name = "is_read")
    private boolean readStatus = false;   // renamed safely

    private LocalDateTime createdAt;

    public Notification() {}

    public Notification(Long userId,
                        NotificationType type,
                        String title,
                        String message) {
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.readStatus = false;
    }

    // Getters & Setters

    public Long getId() { return id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isReadStatus() { return readStatus; }
    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

	
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

   public LocalDateTime getCreatedAt() { return createdAt; }
}