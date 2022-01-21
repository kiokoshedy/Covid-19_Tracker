/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.kiokoCode.covid19tracker.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 *
 * @author DATA INTEGRATED
 */

@Entity
@Data
@ToString
public class CovidStats {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String state;
    private String country;
    private int latestTotalCases;
    private int diffFromPreviousDay;
    private Date todayDate;
    
}
