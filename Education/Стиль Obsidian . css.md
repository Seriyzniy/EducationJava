# 📕Header 1
## 📙Header2
### Header3
#### Header4
##### Header5

> [!NOTE] Title `sadas`
> jnbl
> 

**asdasasdas**
*cursive*

==asdasdasdas==
`asdasdsa`

==Hello **daasdas** ==

# Стиль текста
```css
.callout {
    border: 5px solid #affcae;
    border-radius: 8px;
    background-color: var(--background-secondary);
}

.callout-title {
    color: #affcae !important; /* Оранжевый цвет */
}

/* Выделение текста ==маркером== */
.cm-highlight, mark {
    color: #2b3b30 !important;
    font-weight: 700 !important;
    background-color: #ffe4c4 !important;
}


/* Жирный шрифт */
.cm-strong, strong {
    color: #2979FF !important;
    text-shadow: 1px 0px 0px #062108;
    font-weight: 650 !important;
}

/* Стили для inline-кода, но не внутри callout и блоков кода */
.cm-s-obsidian span.cm-inline-code:not(.cm-formatting),
.markdown-rendered code:not(pre code) {
    color: #c0ebff !important;
    text-shadow: 1px 1px 2px black;
    background-color: #2e3e47 !important;
    border-radius: 10px;
    padding: 2px 6px;
    font-family: 'JetBrains Mono', 'Fira Code', monospace;
}

/* Заголовки */
.cm-header-1, h1 {
    color: #f04a53 !important;
}

.cm-header-2, h2 {
    color: #ff9142 !important;
}

.cm-header-3, h3 {
    color: #c2d66f !important;
}

.cm-header-4, h4 {
    color: #87e1ed  !important;
}

.cm-header-5, h5 {
    color: #b296ff !important;
}
```


# Стиль КОДА
```css
/* Ключевые слова - оранжевые */
.cm-keyword, .token.keyword {
    color: #ce8951 !important;
}

/* Типы данных вообще все*/
.cm-type{
    color: #bcbec4 !important;
}

/* Имена Классов через class*/
.cm-def {
    color: #56a8f5 !important;
}

/* Имена переменных и функций*/
.cm-variable {
    color: #707070 !important;
}



/* Числа - берюзовые */
.cm-number, .token.number {
    color: #4ec9b0 !important;
}

/* Строки - зеленые */
.cm-string, .token.string {
    color: #6a8759 !important;
}

/* Комментарии - темно-серые */
.cm-comment, .token.comment {
    color: #666666 !important;
}

/* Аннотации - желтые */
.cm-meta, .token.meta {
    color: #bbb529 !important;
}

/* Операторы, пунктуация, остальной текст - белые */
.cm-operator, .token.operator, 
.cm-punctuation, .token.punctuation {
    color: #ffffff !important;
}
```

```java
public class Foo{
	private int x = 5;
	String str = "Hello";
	
	void main(){
		System.out.println(5 + "Hello")
	}
}
```