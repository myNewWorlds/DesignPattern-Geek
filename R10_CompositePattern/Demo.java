import java.util.List;

public class Demo {
    private static final long ORGANIZATION_ROOT_ID = 1001;
    private DepartmentRepo departmentRepo;  //依赖注入
    private EmployeeRepo employeeRepo; //依赖注入

    //public方法，供外部调用
    public void buildOrganization() {
        Department rootDepartment = new Department(ORGANIZATION_ROOT_ID);

    }

    //private方法，用于自己的构建
    private void buildOrganization(Department department) {
        List<Long> subDepartmentIds = departmentRepo.getSubDepartmentIds(department.getId());
        for (Long subDepartmentId : subDepartmentIds) {
            //将当前部门添加到父部门
            Department subDepartment = new Department(subDepartmentId);
            department.addSubNode(subDepartment);
            //构建当前部门的子部门
            buildOrganization(department);
        }

        List<Long> departmentEmployeeIds = employeeRepo.getDepartmentEmployeeIds(department.getId());
        for (Long employeeId : departmentEmployeeIds) {
            double salary = employeeRepo.getEmployeeSalary(employeeId);
            //将当前员工统一到部门内
            department.addSubNode(new Employee(employeeId, salary));
        }

    }

}
