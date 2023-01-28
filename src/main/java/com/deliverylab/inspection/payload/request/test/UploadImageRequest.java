package com.deliverylab.inspection.payload.request.test;

import java.io.File;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UploadImageRequest {
    @NotBlank
    private File image;
}
