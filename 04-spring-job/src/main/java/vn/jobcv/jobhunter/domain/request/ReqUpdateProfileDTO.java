package vn.jobcv.jobhunter.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.jobcv.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ReqUpdateProfileDTO {
    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotNull(message = "Tuổi không được để trống")
    private Integer age;

    @NotNull(message = "Giới tính không được để trống")
    private GenderEnum gender;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;
}