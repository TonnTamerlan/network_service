package dbUtil.dao;

import dbUtil.dataSets.Division;
import dbUtil.dataSets.Unit;

public interface UnitDAO {
	
	public void addUnit(Unit unit);
	
	public void getUnitByID(long id);
	
	public void getUnitByNameAndDevision(String nameUnit, Division division);
	
	public void updateUnit(Unit unit);
	
	public void deleteUnit(Unit unit);
	
}
