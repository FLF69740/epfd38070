package com.epfd.dolto;

import com.epfd.dolto.formulary.BusinessFormulary;
import com.epfd.dolto.models.Kid;
import com.epfd.dolto.utils.Utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormularyUnitTest {

    private Kid getAlexi(){
        return new Kid("Doe","alexi","CP", Utils.BOY);
    }

    private Kid getSophie(){
        return new Kid("dOe","SOPHIE","Cm1", Utils.GIRL);
    }

    private Kid getJohn(){
        return new Kid("snow","John","cm2", Utils.BOY);
    }

    private List<Kid> getKids(){
        List<Kid> kids = new ArrayList<>();
        kids.add(getAlexi());
        kids.add(getSophie());
        kids.add(getJohn());
        return kids;
    }

    private String stringNameKid(){
        return "John-Henry DE FONTENAY";
    }

    @Test
    public void testIfKidNamesAreOkForDataBase(){
        List<Kid> kids = getKids();
        assertEquals("Alexi DOE,Sophie DOE,John SNOW", BusinessFormulary.getKidNameList(kids));
    }

    @Test
    public void testIfKidClassroomsAreOkForDataBase(){
        List<Kid> kids = getKids();
        assertEquals("CP,CM1,CM2", BusinessFormulary.getKidClassroomList(kids));
    }

    @Test
    public void testIfKidGenderAreOkForDataBase(){
        List<Kid> kids = getKids();
        assertEquals("BOY,GIRL,BOY", BusinessFormulary.getKidGenderList(kids));
    }

    @Test
    public void testIfKidNameAreSplitted(){
        assertEquals("DE FONTENAY", BusinessFormulary.getStringNameFromKid(stringNameKid()));
    }

    @Test
    public void testIfKidFornameAreSplitted(){
        assertEquals("John-Henry", BusinessFormulary.getStringFornameFromKid(stringNameKid()));
    }


}
