/*
 * The MIT License
 *
 * Copyright (c) 2010, Brad Larson
 * Copyright (c) 2019, CloudBees Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.repo.behaviors;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractDescribableImpl;
import hudson.model.TaskListener;
import hudson.plugins.repo.ProjectState;
import hudson.plugins.repo.RevisionState;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Extension point for implementing commandline additions to the repo command.
 *
 * @param <T> the type
 */
public abstract class RepoScmBehavior<T extends RepoScmBehavior<T>> extends AbstractDescribableImpl<T> {

    /**
     * Decorate the <code>repo init</code> commandline.
     *
     * @param commands the current commandline
     * @param env the environment
     * @param listener for logging
     * @return true if continue with the call false if abort
     * @throws TraitApplicationException if something happened that needs the user's attention
     */
    public boolean decorateInit(@Nonnull final List<String> commands,
                                final EnvVars env,
                                @Nonnull final TaskListener listener)
            throws TraitApplicationException {
        return true;
    }

    /**
     * Decorate the <code>repo sync</code> commandline.
     *
     * @param commands the current commandline
     * @param env the environment
     * @param listener for logging
     * @return true if continue with the call false if abort
     * @throws TraitApplicationException if something happened that needs the user's attention
     *   and aborts all future decorations.
     */
    public boolean decorateSync(@Nonnull final List<String> commands,
                                final EnvVars env,
                                @Nonnull final TaskListener listener)
            throws TraitApplicationException {
        return true;
    }

    /**
     * Called just after a successful <code>repo init</code>.
     *
     * @param workspace the workspace where repo init was performed.
     * @param env the environment
     * @param listener for logging
     * @return true if continue with the call false if abort
     * @throws TraitApplicationException if something happened that needs the user's attention
     *      and aborts all future operations.
     */
    public boolean postInit(final FilePath workspace,
                            final EnvVars env,
                            @Nonnull final TaskListener listener) throws TraitApplicationException {
        return true;
    }

    /**
     * Called just before <code>repo sync</code>.
     *
     * @param executable the path/name of the repo executable
     * @param launcher a launcher to launch the executable with
     * @param workspace the current workspace
     * @param listener the ways of logging to the build
     * @param env the current environment
     * @return true if continue with the call false if abort
     *
     * @throws TraitApplicationException if something happened that needs the user's attention
     *      and aborts all future operations.
     */
    public boolean preSync(@Nonnull final String executable,
                           @Nonnull final Launcher launcher,
                           @Nonnull final FilePath workspace,
                           @Nonnull final TaskListener listener,
                           final EnvVars env) throws TraitApplicationException {
        return true;
    }

    /**
     * Called when calculating if changes are significant to warrant a rebuild.
     *
     * The first extension to say <code>true</code> gets it.
     *
     * @param changedProjects list of projects (repositories) that have changed
     * @param current the current revision
     * @param baseline the previous revision
     * @return true if all changes warrants to ignore a rebuild, false otherwise (the default)
     */
    public boolean shouldIgnoreChanges(final List<ProjectState> changedProjects,
                                       final RevisionState current,
                                       final RevisionState baseline) {
        return false;
    }
}
