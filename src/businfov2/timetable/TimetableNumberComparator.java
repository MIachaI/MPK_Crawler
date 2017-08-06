package businfov2.timetable;

import java.util.Comparator;

public class TimetableNumberComparator implements Comparator<Timetable> {

    @Override
    public int compare(Timetable timetable, Timetable t1) {
        String l1 = timetable.lineNumber;
        String l2 = t1.lineNumber;
        // check if any of the numbers contains letters
        String containingLetterRegex = "\\D";
        String onlyDigitsRegex = "^\\d+$";
//        Pattern containingLetter = Pattern.compile(containingLetterRegex);
//        Pattern onlyDigits = Pattern.compile(onlyDigitsRegex);
        // if both are numbers - return difference
        if(l1.matches(onlyDigitsRegex) && l2.matches(onlyDigitsRegex)){
            return Integer.parseInt(l1) - Integer.parseInt(l2);
        }
        // if both are strings - compare strings
        if(!(l1.matches(onlyDigitsRegex) || l2.matches(onlyDigitsRegex))){
            return l1.compareTo(l2);
        }
        return - l1.compareTo(l2);
    }
}