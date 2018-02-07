package dbService.dataSets;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

/**
 * Class contains information about unit
 * 
 * @author Alexey Kopylov
 *
 *@version 1.0
 */

@Entity
@Table(name = "units")
public class Unit implements Serializable {

	private static final long serialVersionUID = 6066299839648490881L;

	@Id
	@Column(name = "unit_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long unit_id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "photos")
	private String photos;

	@Column(name = "sources")
	private String sources;
	
	@ManyToOne
	@JoinColumn(name = "division_id")
	private Division division;
	
	@OneToMany(mappedBy = "unit", fetch = FetchType.LAZY)
	private Set<Equipment> equipments = new HashSet<>();

	public Unit() {
	}

	public long getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(long unit_id) {
		this.unit_id = unit_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public String getSources() {
		return sources;
	}

	public void setSources(String sources) {
		this.sources = sources;
	}

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public Set<Equipment> getEquipments() {
		return equipments;
	}

	public void setEquipments(Set<Equipment> equipments) {
		this.equipments = equipments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (unit_id ^ (unit_id >>> 32));
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
		Unit other = (Unit) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (unit_id != other.unit_id)
			return false;
		return true;
	}
	
}
