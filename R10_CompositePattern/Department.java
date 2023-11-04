import java.util.ArrayList;
import java.util.List;

public class Department extends HumanResource {

    private List<HumanResource> subNodes = new ArrayList<>();

    public Department(long id) {
        super(id);
    }

    @Override
    public double calculateSalary() {
        double salary = 0;
        for (HumanResource subNode : subNodes) {
            salary += subNode.calculateSalary();
        }
        this.salary = salary;
        return salary;
    }

    public void addSubNode(HumanResource humanResource) {
        subNodes.add(humanResource);
    }
}
