package org.example.dynamicqueues.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: otabek
 * Date: 18/12/23 14:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TgPojo {


    private String text;
    private Long priority;

}
