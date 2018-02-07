package dbService.dataSets;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Class contains information about equipment
 * 
 * @author Alexey Kopylov
 *
 *@version 1.0
 */

@Entity
@Table(name = "equipments")
public class Equipment implements Serializable {

	private static final long serialVersionUID = -1870858169605152340L;

	@Id
	@Column(name = "equipment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long equipment_id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "ip", nullable = false)
	private String ip;

	@Column(name = "discription", length = 1000)
	private String discription;

	@ManyToOne
	@JoinColumn(name = "unit_id")
	private Unit unit;
	
	public Equipment() {
	}

	public long getEquipment_id() {
		return equipment_id;
	}

	public void setEquipment_id(long equipment_id) {
		this.equipment_id = equipment_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (equipment_id ^ (equipment_id >>> 32));
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
		Equipment other = (Equipment) obj;
		if (equipment_id != other.equipment_id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
}
