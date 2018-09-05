package com.conext.conext.utils;

import com.conext.conext.model.AllSkill;

import java.util.ArrayList;

/**
 * Created by Ashith VL on 6/17/2017.
 */


public interface IFragmentToActivity {

    void passSkill(ArrayList<AllSkill> msg);

    void passSkillMentor(ArrayList<AllSkill> msg);

    void passAllUserSkill(ArrayList<AllSkill> allSkills);

}