package db;

public class QueryControl {

	/**
	 * Centralizes access to various query classes.
	 * All queries public static instance here (maybe change later, so we wont create new instance every time)
	 * {@link UserQuery} for user-related queries
	 * {@link OrderQuery} for order-related queries
	 * {@link ServerUtilsQueries} for server utility queries
	 * {@link ManagersQuery} for manager-related queries
	 * {@link EmployeeQuery} for employee-related queries
	 * {@link SupplierQuery} for supplier-related queries
	 */
	public static UserQuery userQueries = new UserQuery();
	public static OrderQuery orderQueries = new OrderQuery();
	public static ServerUtilsQueries serverQueries = new ServerUtilsQueries();
	public static ManagersQuery managersQuery = new ManagersQuery();
	public static EmployeeQuery employeeQuery=new EmployeeQuery();
	public static SupplierQuery supplierQuery=new SupplierQuery();
}
