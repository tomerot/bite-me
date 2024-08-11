package db;

public class QueryControl {

	
	/**
	 * Centralizes access to various query classes.
	 * All queries public static instance here (maybe change later, so we wont create new instance every time)
	 */
	public static UserQuery userQueries = new UserQuery();
	public static OrderQuery orderQueries = new OrderQuery();
	public static EmployeeQuery employeeQuery=new EmployeeQuery();
	public static SupplierQuery supplierQuery=new SupplierQuery();
	public static ServerUtilsQueries serverQueries = new ServerUtilsQueries();
	public static ManagersQuery managersQuery = new ManagersQuery();

}
