# WikiMiner

In this project, common noun mentions are extracted from all the wikipedia page articles. Latest (March 06, 2016) wikipedia dump is downloaded from [here](https://dumps.wikimedia.org/enwiki/latest/).

To get wikipedia article content, [wikixmlj](https://github.com/synhershko/wikixmlj) is used. Then all the mentions are extracted and tagged with appropriate part-of-speech using [stanford log-linear POS tagger](http://nlp.stanford.edu/software/tagger.shtml). Finally common noun mentions are derived from the list of noun mentions by following a simple rule - *all non-capitalized nouns are common nouns*.

To run the program, the number of threads need to be given as a command line argument. This program takes around **26** cpu hours (with 2.5GHz CPU and 16GB RAM) with **32** threads to generate the common noun mentions from **9,922,042** wikipedia page articles. A detail of the dataset can be found inside the data folder.

A xml file containing all the common noun mentions can be found [here](https://www.dropbox.com/s/4s2fu9d4wp06llf/common_nouns.xml.bz2?dl=0). The xml file is organized as below.

```xml
<?xml version="1.0" encoding="utf-8"?>
<nouns>
	<article id="7599" title="Information retrieval">
		<row id="1" tag="1">
			<mention>information</mention>
			<pageTitle>Information</pageTitle>
		</row>
		<row id="2" tag="1">
			<mention>metadata</mention>
			<pageTitle>Metadata</pageTitle>
		</row>
		<row id="3" tag="1">
			<mention>full-text</mention>
			<pageTitle>Full_text_search</pageTitle>
		</row>
	</article>
</nouns>
```

Each **article** tag represents one wikipedia page article and each **row** tag represents one common noun mention. Attribute values in those tags are described below.

#### Article tag attributes
* **id** is the line number of an article title in this [file](https://www.dropbox.com/s/ctkxby3a3tmrrwa/wiki_page_titles%28no%20redirection%29.txt?dl=0).
* **title** is the wikipedia article title.

#### Row tag attributes
* **id** is a sequence number for the mentions. In the above example, **information** is the first and **metadata** is the second mention in the wikipedia article. Note. only noun mentions are included here.
* **tag** represents whether the mention is a common noun or not. "1" represents common noun and "0" represents not common noun but noun.
* **mention** represents nouns which are linked to another wikipedia page.
* **pageTitle** is the title of the linked wikipedia page. By concatenating https://en.wikipedia.org/wiki/ to the pageTitles, we can get the URL for the wikipedia pages. For example, https://en.wikipedia.org/wiki/Full_text_search.

The above example is a snippet from the representation of [Information retrieval](https://en.wikipedia.org/wiki/Information_retrieval) wikipedia page.

#### Data Statistics
| Particulars | Count |
| :--- | :--- |
| Number of pages in Wikipedia dump | 16,388,870 |
| Number of pages which are redirected to another page | 6,466,828 |
| Number of non-redirected pages in Wikipedia | 9,922,042 |
| Total (unique) Noun mentions found | 26,660,798 |
| Total (unique) Common Noun mentions found | 2,512,347 |
