# Wikipedia Taxonomy
This project aims to analyze the Wikipedia category taxonomy and observe the relations between Wiki entities, categories and mentions.
Below I am discussing the working procedure step by step.

## Key Terms
[Wikipedia Articles](https://en.wikipedia.org/wiki/Wikipedia:What_is_an_article%3F), [Wikipedia Category](https://en.wikipedia.org/wiki/Help:Category)

## Download Dataset
In this project, i am using the latest (September 02, 2016) wikipedia dump which can be downloaded from [here](https://dumps.wikimedia.org/enwiki/latest/enwiki-latest-pages-articles.xml.bz2).

## Data Statistics
| Particulars | Count |
| :--- | :--- |
| Number of pages in Wikipedia dump | 16,857,586 |
| Number of pages which are redirected to another page | 6,762,168 |
| Number of non-redirected pages | 10,095,418 |
| Number of special pages | 4,226,679 |
| Number of disambiguation pages | 293,691 |
| Number of articles | 6,062,625 |
| Number of category pages | 1,449,680 |

## Wikipedia Articles
I am assuming Wikipedia articles are the Wikipedia pages that are,
 - not [redirected](https://en.wikipedia.org/wiki/Wikipedia:Redirect) to other page
 - not [special pages](https://en.wikipedia.org/wiki/Help:Special_page)
 - not [disambiguation pages](https://en.wikipedia.org/wiki/Category:Disambiguation_pages)

I have extracted the Wikipedia articles and stored them in XML format like following.

```xml
<?xml version="1.0" encoding="utf-8"?>
<article-pages>
	<page id="12" title="Anarchism">
		<categories>Anarchism, Political culture, Political ideologies, Social theories, Anti-fascism, Anti-capitalism, Far-left politics</categories>
	</page>
	<page id="25" title="Autism">
		<categories>Autism, Communication disorders, Mental and behavioural disorders, Neurological disorders, Neurological disorders in children, Pervasive developmental disorders, Psychiatric diagnosis</categories>
	</page>
	<page id="39" title="Albedo">
		<categories>Climate forcing, Climatology, Electromagnetic radiation, Land surface effects on climate, Radiometry, Scattering, absorption and radiative transfer (optics), Radiation</categories>
	</page>
<article-pages>
```

Each **page** tag represents one wikipedia article and the **category** tag represents all the categories in which this article belongs to. Get the complete set of Wikipedia articles from [here](https://drive.google.com/file/d/0B8ZGlkqDw7hFc01GMmh3Wm9PQ2s/view?usp=sharing).

## Wikipedia Categories
To build the Wikipedia category taxonomy, I parsed the Wikipedia en XML dump looking for articles whose title begin with **Category:**. Then i retrieved all the parent categories of each category from the xml dump. I used this relationship to build the taxonomic structure.

```xml
<?xml version="1.0" encoding="utf-8"?>
<category-pages>
	<page id="690070" title="Futurama">
		<categories>Television series created by Matt Groening, Comic science fiction, Wikipedia categories named after American animated television series, 20th Century Fox franchises, Transhumanism in television series, Robots in television, Comedy by franchise, Science fiction by franchise, Fox animation, Fox network shows, Comedy Central shows</categories>
	</page>
	<page id="690451" title="World War II">
		<categories>Wikipedia categories named after wars, 20th-century conflicts, 1930s conflicts, 1940s conflicts, Conflicts in 1939, Conflicts in 1940, Conflicts in 1941, Conflicts in 1942, Conflicts in 1943, Conflicts in 1944, Conflicts in 1945, Global conflicts, Modern Europe, The World Wars</categories>
	</page>
	<page id="690571" title="Programming languages">
		<categories>Computer languages, Computer programming, Programming language topics, Programming language theory</categories>
	</page>
<category-pages>
```

Each **page** tag represents one wikipedia category page (every category in wiki is associated with a page) and the **categories** tag represents all the parent categories. Get the complete set of Wikipedia categories from [here](https://drive.google.com/file/d/0B8ZGlkqDw7hFVWRfSHlWdW9URjA/view?usp=sharing).


