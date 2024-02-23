select "product"."id_",
"product"."name_",
(select filename_ from file where container_ = product.id_ and file.is_removed_ = false  order by id_ limit 1) as filename_
from "product"
where "product"."category_" = 1458  and "product"."is_removed_" = false
order by name_;