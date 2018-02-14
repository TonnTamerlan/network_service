package dbUtil.dataSets;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Class contains information about unit
 * 
 * @author Alexey Kopylov
 *
 * @version 1.0
 */

@Entity
@Table(name = "units")
public class Unit implements Serializable {

	private static final long serialVersionUID = 6066299839648490881L;

	@NotNull
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Column(name = "name", nullable = false)
	private String name;
	
	@NotNull
	@Column(name = "adress", nullable = false)
	private String adress;

	@Column(name = "photos")
	private String photos;

	@Column(name = "sources")
	private String sources;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "division_id")
	private Division division;

	@OneToMany(mappedBy = "unit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Equipment> equipments = new HashSet<>();

	public Unit() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Unit(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
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
		if (division != null) {
			division.getUnits().add(this);
		}
	}

	public Set<Equipment> getEquipments() {
		return equipments;
	}
	
	public void addEquipment(Equipment equip) {
		if (equip == null) {
			return;
		}
		if (!this.equipments.contains(equip)) {
			equipments.add(equip);
		}
		equip.setUnit(this);
	}

	public void setEquipments(Set<Equipment> equipments) {
		this.equipments = equipments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adress == null) ? 0 : adress.hashCode());
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
		Unit other = (Unit) obj;
		if (adress == null) {
			if (other.adress != null)
				return false;
		} else if (!adress.equals(other.adress))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Unit =\n")
				.append("{\n")
				.append("\t\"id\": ").append(this.getId()).append("\",\n")
				.append("\t\"name\": \"").append(name).append("\",\n")
				.append("\t\"adress\": \"").append(adress).append("\",\n")
				.append("\t\"division\": \"").append(division).append("\"\n")
				.append("\t\"equipments\":\n\t{\n");
		if (equipments != null && !equipments.isEmpty()) {
			for (Equipment equip : equipments) {
				str.append("\t\t\"").append(equip.getName()).append("\"\n");
			}
		}
		str.append("\t}\n").append("}");
		return str.toString();
	}

}
