package org.chi.dpuk.sensing.platform.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.chi.dpuk.sensing.platform.view.View;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * The Participant entity representing a participant of the Sensing Platform.
 */
// We do not need to index the id column twice as marking a column with @Id
// annotation creates a unique primary key which is already indexed.
@Table(name = "participant")
@Entity
public class Participant {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@JsonView(View.Public.class)
	private Long id;

	@JsonView(View.Public.class)
	private String name;

	@JsonView(View.Public.class)
	private String address;

	@Size(min = 5)
	@JsonView(View.Public.class)
	private String contactNumber;

	@Size(min = 10, max = 10)
	@JsonView(View.Public.class)
	private String nhsNumber;

	@JsonView(View.Participant.class)
	@ManyToOne(fetch = FetchType.EAGER)
	private Study study;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "participant")
	@JsonView(View.Participant.class)
	@OrderBy("id asc")
	private Set<DataSource> dataSources = new HashSet<DataSource>();

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Set<DataSource> getDataSources() {
		return dataSources;
	}

	public void setDataSources(Set<DataSource> dataSources) {
		this.dataSources = dataSources;
	}

	public String getNhsNumber() {
		return nhsNumber;
	}

	public void setNhsNumber(String nhsNumber) {
		this.nhsNumber = nhsNumber;
	}

	@Override
	public String toString() {
		return "Participant [id=" + id + ", name=" + name + ", address=" + address + ", contactNumber=" + contactNumber
				+ ", nhsNumber=" + nhsNumber + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Participant other = (Participant) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	// Utility method to add dataSource to existing dataSources for a
	// participant
	public void addDataSource(DataSource dataSource) {
		Set<DataSource> existingDataSources = this.getDataSources();
		if (existingDataSources.isEmpty()) {
			existingDataSources = new HashSet<DataSource>();
		}
		existingDataSources.add(dataSource);
		this.setDataSources(existingDataSources);
	}

}