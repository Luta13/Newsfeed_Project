package org.sparta.newsfeed.newsfeed.dto;

import lombok.Getter;
import lombok.Setter;
import org.sparta.newsfeed.newsfeed.enums.NewsfeedSortEnum;

@Getter
@Setter // ModelAttribute를 사용하는 경우 개별적으로 받기 위해 NoArgsConstructor를 이용하고 Setter로 값을 넣어준다.
public class NewsfeedRequestDto {
    private int page = 1;
    private NewsfeedSortEnum sort;
    private String startDt;
    private String endDt;
}
