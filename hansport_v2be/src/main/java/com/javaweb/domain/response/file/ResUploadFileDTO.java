package com.javaweb.domain.response.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResUploadFileDTO {
<<<<<<< HEAD
    private List<String> fileName;
=======
    private String fileName;
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
    private Instant upLoadedAt;
}
