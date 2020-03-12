package dapartment;

import atm.ATM.ATM;

public class DepartmentImplDecorator implements Department {
    Department originalDepartment;

    public DepartmentImplDecorator(Department originalDepartment) {
        this.originalDepartment = originalDepartment;
    }

    @Override
    public String getTotalBalance() {
        return "Total balance: " + originalDepartment.getTotalBalance();
    }

    @Override
    public void resetAllATM() {
        originalDepartment.resetAllATM();
    }

    public void registerATM(ATM atm) {
        originalDepartment.registerATM(atm);
    }

    @Override
    public void removeATM(ATM atm) {
        originalDepartment.removeATM(atm);
    }
}
