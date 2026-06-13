package JAVA_Olinski_Olma_zaliczenie.factory;

import JAVA_Olinski_Olma_zaliczenie.model.Assignment;
import JAVA_Olinski_Olma_zaliczenie.model.AssignmentType;
import JAVA_Olinski_Olma_zaliczenie.priority.HighPriority;
import JAVA_Olinski_Olma_zaliczenie.priority.LowPriority;
import JAVA_Olinski_Olma_zaliczenie.priority.MediumPriority;

public class AssignmentFactory {

    /**
     * Główna metoda wzorca projektowego Factory.
     * Ukrywa przed serwisem logikę dobierania polimorficznych obiektów priorytetu.
     */
    public static Assignment createAssignment(AssignmentType type, String title, String description) {
        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setAssignmentType(type);

        if (type == null) {
            assignment.setPriority(new LowPriority());
            return assignment;
        }

        switch (type) {
            case EXAM:
                assignment.setPriority(new HighPriority());
                break;
            case QUIZ:
                assignment.setPriority(new MediumPriority());
                break;
            case HOMEWORK:
            default:
                assignment.setPriority(new LowPriority());
                break;
        }

        return assignment;
    }
}