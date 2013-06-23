require 'rake'

bundle_path = "_vendor/bundle"
repo_url = "https://github.com/car-io/car.io-server.git"
branch = "gh-pages"
deploy_dir= "_deploy"


task :default => %w[ jekyll:build ]

namespace :bundle do
    task :install do
        system "bundle install --path #{bundle_path}"
    end
end

namespace :jekyll do
    task :build, :target do | t, args |
        args.with_defaults(:target => "_site")
        puts "Building site in #{args.target}"
        system "bundle exec jekyll build -d #{args.target}"
    end
    task :serve do
        system "bundle exec jekyll serve -w"
    end
end

namespace :deploy do

    task :deploy, [deploy_dir] => %w[ deploy:clone,
        deploy:clean, jekyll:build, deploy:commit ]

    task :clone do
        if File.directory? deploy_dir
            Dir.chdir deploy_dir do
                puts "Resetting #{deploy_dir}."
                system "git reset --hard HEAD"
                puts "Fetching remote changes of #{deploy_dir}"
                system "git pull"
            end
        else
            puts "Cloning remote to #{deploy_dir}"
            system "git clone #{repo_url} -b #{branch} #{deploy_dir}"
        end
    end

    task :clean do
        Dir.glob("#{deploy_dir}/*") do |item|
            puts "Removing #{item}"
            FileUtils.rm_rf(item)
        end
    end

    task :commit do
        #Dir.chdir deploy_dir do
        #    system "git add --ignore-removal ."
        #    system "git add --update :/"
        #    system "git ci && git push origin gh-pages"
        #end
    end
end
