package org.chi.dpuk.sensing.platform.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.chi.dpuk.sensing.platform.view.View;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * The Study entity representing a study within the Sensing Platform.
 */

@Table(name = "study", indexes = { @Index(name = "nameIndex", columnList = "name") })
@Entity
public class Study {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@JsonView(View.Public.class)
	private Long id;

	@NotNull
	@Size(min = 3)
	@Column(unique = true)
	@JsonView(View.Public.class)
	private String name;

	@JsonView(View.Public.class)
	private String description;

	@ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinTable(name = "study_user", joinColumns = { @JoinColumn(name = "study_id") }, inverseJoinColumns = {
			@JoinColumn(name = "user_id") })
	@JsonView(View.Study.class)
	@OrderBy("id asc")
	private Set<User> users = new HashSet<User>();

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "study")
	@JsonView(View.Study.class)
	@OrderBy("id asc")
	private Set<Participant> participants = new HashSet<Participant>();

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<Participant> participants) {
		this.participants = participants;
	}

	@Override
	public String toString() {
		return "Study [id=" + id + ", name=" + name + ", description=" + description + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Study other = (Study) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	// Utility method to add participant to existing participants for a study
	public void addParticipant(Participant participant) {
		Set<Participant> existingParticipants = this.getParticipants();
		if (existingParticipants.isEmpty()) {
			existingParticipants = new HashSet<Participant>();
		}
		existingParticipants.add(participant);
		this.setParticipants(existingParticipants);
	}

	// Utility method to add user to existing users for a study
	public void addUser(User user) {
		Set<User> existingUsers = this.getUsers();
		if (existingUsers.isEmpty()) {
			existingUsers = new HashSet<User>();
		}
		existingUsers.add(user);
		this.setUsers(existingUsers);
	}

}