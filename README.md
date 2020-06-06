# DB demo
clone from https://cstack.github.io/db_tutorial using java programming

![CI](https://github.com/redsnow1992/db-demo/workflows/CI/badge.svg)

## develop
### 1. disk layout
1. file

| range | value |
| --- | --- |
| [0, 4) | num of rows in table |
| [4, page_num * page_size) | |

2. page

| range | value |
| --- | --- |
| [0, 4) | num of row in page |
| [4, page_num * page_size) | |

### 2. run test
go to project directory
```
bundle exec rspec
```
