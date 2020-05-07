package org.chi.dpuk.sensing.platform.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.chi.dpuk.sensing.platform.view.View;
import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * The User entity representing a user of the Sensing Platform (e.g. a
 * researcher or administrator).
 */

@Table(name = "user", indexes = { @Index(name = "userNameIndex", columnList = "userName") })
@Entity
public class User {

	public static enum Role {
		ROLE_ADMIN, ROLE_RESEARCHER_ADMIN, ROLE_RESEARCHER, ROLE_SERVICE
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@JsonView(View.Public.class)
	private Long id;

	@NotNull
	@Size(min = 5)
	@Column(unique = true)
	@JsonView(View.Public.class)
	private String userName;

	@NotNull
	@Size(min = 10, max = 72)
	@JsonView(View.Public.class)
	private String passwordHash;

	@Email
	@JsonView(View.Public.class)
	private String email;

	@JsonView(View.Public.class)
	private String title;

	@JsonView(View.Public.class)
	private String firstName;

	@JsonView(View.Public.class)
	private String lastName;

	@Enumerated(EnumType.STRING)
	@NotNull
	@JsonView(View.Public.class)
	private Role role;

	@JsonView(View.Public.class)
	private String passwordResetGUID;

	@JsonView(View.Public.class)
	private Date passwordResetDate;

	@ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinTable(name = "study_user", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "study_id") })
	@JsonView(View.User.class)
	@OrderBy("id asc")
	private Set<Study> studies = new HashSet<Study>();

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@JsonView(View.Public.class)
	public String getFullName() {
		return (this.getFirstName() != null && this.getFirstName().length() > 0 ? this.getFirstName() + " " : "")
				+ (this.getLastName() != null && this.getLastName().length() > 0 ? this.getLastName() : "");
	}

	@JsonView(View.Public.class)
	public String getFullNameWithTitle() {
		return (this.getTitle() != null && this.getTitle().length() > 0 ? this.getTitle() + " " : "")
				+ (this.getFirstName() != null && this.getFirstName().length() > 0 ? this.getFirstName() + " " : "")
				+ (this.getLastName() != null && this.getLastName().length() > 0 ? this.getLastName() : "");
	}

	public Set<Study> getStudies() {
		return studies;
	}

	public void setStudies(Set<Study> studies) {
		this.studies = studies;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPasswordResetGUID() {
		return passwordResetGUID;
	}

	public void setPasswordResetGUID(String passwordResetGUID) {
		this.passwordResetGUID = passwordResetGUID;
	}

	public Date getPasswordResetDate() {
		return passwordResetDate;
	}

	public void setPasswordResetDate(Date passwordResetDate) {
		this.passwordResetDate = passwordResetDate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", passwordHash=" + passwordHash + ", email=" + email
				+ ", title=" + title + ", firstName=" + firstName + ", lastName=" + lastName + ", role=" + role
				+ ", passwordResetGUID=" + passwordResetGUID + ", passwordResetDate=" + passwordResetDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	// Utility method to add study to existing studies for a user
	public void addStudy(Study study) {
		Set<Study> existingStudies = this.getStudies();
		if (existingStudies.isEmpty()) {
			existingStudies = new HashSet<Study>();
		}
		existingStudies.add(study);
		this.setStudies(existingStudies);
	}

}