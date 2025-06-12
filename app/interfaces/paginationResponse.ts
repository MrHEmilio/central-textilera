export interface PaginationResponse<T> {
  content: T;
  pagination: Pagination;
}

export interface Pagination {
  page: number;
  size: number;
  totalPage: number;
  totalRecords: number;
}
