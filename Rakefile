require 'rake'

bundle_path = "_vendor/bundle"
repo_url = "https://#{ENV["GITHUB_TOKEN"]}@github.com/car-io/car.io-server.git"
branch = "gh-pages"
deploy_dir =  "_deploy"
github_name = "Travis CI"
github_mail = "travis@travis-ci.org"

task :default => %w[ build ]

namespace :bundle do
    task :install do
        system "bundle install --path #{bundle_path}"
    end
end

task :build, :target do | t, args |
	args.with_defaults(:target => "_site")
	puts "Building site in #{args.target}"
	system "bundle exec jekyll build -d #{args.target}"
end
task :serve do
	system "bundle exec jekyll serve -w"
end

task :travis do | t |
    system "git config --global user.name \"#{github_name}\""
    system "git config --global user.email \"#{github_mail}\""
    system "git clone --quiet --branch=#{branch} #{repo_url} #{deploy_dir} > /dev/null"
    Dir.glob("#{deploy_dir}/*") do | item |
        FileUtils.rm_rf(item)
    end
    system "bundle exec jekyll build -d #{deploy_dir}"
    Dir.chdir deploy_dir do
        system "git add --ignore-removal ."
        system "git add --update :/"
        system "git ci -m \"Updating gh-pages to #{ENV["GIT_COMMIT_ID"]}\" && git push origin gh-pages"
    end
end
