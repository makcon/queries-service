package makcon.queries.app.mother

import makcon.queries.app.utils.Rand
import makcon.queries.dto.*
import makcon.queries.dto.constant.FilterFieldV1

object QueryV1Mother {
    
    fun of(
        filters: Set<QueryFilterV1> = setOf(QueryFilterV1Mother.of()),
        paging: PagingV1 = PagingV1Mother.ofFirstTen(),
        sorting: SortingV1? = SortingV1Mother.of(),
        resultFields: Set<String> = setOf(ResultFieldV1.COUNT, ResultFieldV1.ITEMS),
        connector: String = QueryConnectorV1.AND,
    ) = QueryV1(
        filters = filters,
        paging = paging,
        sorting = sorting,
        resultFields = resultFields,
        connector = connector,
    )
}

object QueryFilterV1Mother {
    
    fun of(
        field: String = Rand.string(),
        value: Any = Rand.string(),
        operator: String = QueryOperatorV1.EQ,
    ) = QueryFilterV1(
        field = field,
        value = value,
        operator = operator,
    )
    
    fun ofEQ(
        field: String = Rand.string(),
        value: Any = Rand.string(),
    ) = of(field, value, QueryOperatorV1.EQ)
}

object PagingV1Mother {
    
    fun of(
        number: Int = Rand.int(),
        size: Int = Rand.int(),
    ) = PagingV1(
        number = number,
        size = size
    )
    
    fun ofFirstTen() = PagingV1(
        number = 0,
        size = 10
    )
    
    fun ofSize(size: Int) = PagingV1(
        number = Rand.int(),
        size = size
    )
}

object SortingV1Mother {
    
    fun of(
        field: String = FilterFieldV1.ID,
        dir: String = SortDirV1.ASC,
    ) = SortingV1(
        field = field,
        dir = dir
    )
}