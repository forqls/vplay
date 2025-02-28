package pocopoco_vplay.commom.model.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageInfo {
    private int currentPage;
    private int listCount;
    private int PageLimit;
    private int maxPage;
    private int startPage;
    private int endPage;
    private int boardList;
}
