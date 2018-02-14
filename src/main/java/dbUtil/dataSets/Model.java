package dbUtil.dataSets;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 * Abstract class for dataset
 * 
 * @author Alexey Kopylov
 *
 * @version 1.0
 */


@MappedSuperclass
public abstract class Model implements Serializable {

	private static final long serialVersionUID = 3769421371003596482L;

	@NotNull
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	public Model() {
	}

	public Model(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
