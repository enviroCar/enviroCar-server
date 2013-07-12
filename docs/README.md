# enviroCar Server Documentation

The documentation is build using [Jekyll][jekyll]. Changes to this 
directory are automatically build and deployed to the 
[GitHub Pages][gh-pages] once the commits hit master.

## How to build the documentation locally
First you have to install [Ruby][ruby]. After that 
switch to this directoy and run the following commands:
```
gem install bundler
bundle install --path _vendor/bundle
bundle exec jekyll serve -w
```

The documentation site will now be available at `http://localhost:4000/`.
You may have to adjust URLs manually as there is no URL rewriting in 
the included web server. E.g. adjust `/api/users` to `/api/users.html`.

[gh-pages]: http://envirocar.github.io/enviroCar-server/ "GitHub Pages"
[jekyll]: http://jekyllrb.com/ "Jekyll"
[ruby]: http://www.ruby-lang.org/en/ "Ruby"