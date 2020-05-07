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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.chi.dpuk.sensing.platform.view.View;

import com.fasterxml.jackson.annotation.JsonView;

@Table(name = "dataSource", indexes = { @Index(name = "nameIndex", columnList = "name") })
@Entity
public class DataSource {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@JsonView(View.Public.class)
	private Long id;

	@NotNull
	@Size(min = 3)
	@JsonView(View.Public.class)
	private String name;

	@NotNull
	@JsonView(View.Public.class)
	private String description;

	@JsonView(View.Public.class)
	private String type;

	@JsonView(View.Public.class)
	@NotNull
	@Size(min = 5, max = 32)
	@Column(unique = true)
	private String userName;

	@NotNull
	@Size(min = 10, max = 72)
	private String password;

	@JsonView(View.DataSource.class)
	@ManyToOne(fetch = FetchType.EAGER)
	private Participant participant;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "dataSource")
	@OrderBy("id asc")
	private Set<DataSet> dataSets = new HashSet<DataSet>();

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public Set<DataSet> getDataSets() {
		return dataSets;
	}

	public void setDataSets(Set<DataSet> dataSets) {
		this.dataSets = dataSets;
	}

	@Override
	public String toString() {
		return "DataSource [id=" + id + ", name=" + name + ", description=" + description + ", type=" + type
				+ ", userName=" + userName + ", password=" + password + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
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
		DataSource other = (DataSource) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
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

	// Utility method to add dataSet to existing dataSets for a dataSource
	public void addDataSet(DataSet dataSet) {
		Set<DataSet> existingDataSets = this.getDataSets();
		if (existingDataSets.isEmpty()) {
			existingDataSets = new HashSet<DataSet>();
		}
		existingDataSets.add(dataSet);
		this.setDataSets(existingDataSets);
	}

}